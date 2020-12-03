package net.corda.examples.yo.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.contracts.StateRef;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.flows.*;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.examples.yo.contracts.AttachmentContract;
import net.corda.examples.yo.states.AttachmentState;

import java.util.*;

@StartableByRPC
@InitiatingFlow
public class StoreHash extends FlowLogic<String> {


    public String attachmentHash;

    public StoreHash(String attachmentHash) {
        this.attachmentHash = attachmentHash;
    }

    @Suspendable
    @Override
    public String call() throws FlowException {


        Optional<StateAndRef<AttachmentState>> state = getServiceHub().getVaultService().queryBy(AttachmentState.class)
                .getStates().stream().filter(fil -> fil.getState().getData().getAttachmentHash().equals(attachmentHash)).findFirst();


        if (state.isPresent()){
            return "hash already exists";
        }
        else {
            AttachmentState attachmentState = new AttachmentState(attachmentHash,new UniqueIdentifier());
            attachmentState.setCurrentParty(getOurIdentity());
            CordaX500Name counterPatyName ;
            System.out.println("x500 name : "+getOurIdentity().getName().toString());
            System.out.println("if condition "+getOurIdentity().getName().getOrganisation().equals("PartyA"));
            CordaX500Name temp = CordaX500Name.parse("O=PartyA,L=London,C=GB") ;
            if (getOurIdentity().getName().getOrganisation().equals("PartyA")){
                System.out.println("inside if ");
                counterPatyName = CordaX500Name.parse("O=PartyB,L=New York,C=US");
                attachmentState.setCounterParty(getServiceHub().getNetworkMapCache().getPeerByLegalName(counterPatyName));
            }
            else {
                System.out.println("inside else");
                counterPatyName = CordaX500Name.parse("O=PartyA,L=London,C=GB");
                attachmentState.setCounterParty(getServiceHub().getNetworkMapCache().getPeerByLegalName(counterPatyName));
            }
            Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
            System.out.println("counter party after setting"+attachmentState.getCounterParty().getOwningKey().getEncoded());
            TransactionBuilder builder = new TransactionBuilder(notary)
                    .addOutputState(attachmentState)
                    .addCommand(new AttachmentContract.Commands.Create(),getOurIdentity().getOwningKey());

            System.out.println("counter party name : "+counterPatyName.getCommonName());
            HashSet<FlowSession> flowSessions = new HashSet<>();
            flowSessions.add(initiateFlow(attachmentState.getCounterParty()));
            SignedTransaction signedTransaction = getServiceHub().signInitialTransaction(builder,getOurIdentity().getOwningKey());
            subFlow(new FinalityFlow(signedTransaction, flowSessions));

            StateRef stateRef = new StateRef(signedTransaction.getId(),0);
            StateAndRef<AttachmentState> transaction = getServiceHub().toStateAndRef(stateRef);
            System.out.println("state from vault "+transaction.getState().getData().getAttachmentHash());
            System.out.println("the provided hash doesnt exist on vault"+ transaction.getState().getData().getParticipants());
            return signedTransaction.getId().toString();
        }


    }
}
