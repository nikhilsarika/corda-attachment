package net.corda.examples.yo.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.flows.*;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.examples.yo.contracts.AttachmentContract;
import net.corda.examples.yo.states.AttachmentState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

        Map<String,Boolean> responseMap = new HashMap<>();
        if (state.isPresent()){
            return "hash alrteady exists";
        }
        else {
            AttachmentState attachmentState = new AttachmentState(attachmentHash,new UniqueIdentifier());
            attachmentState.setCurrentParty(getOurIdentity());
            CordaX500Name counterParty ;
            System.out.println("x500 name : "+getOurIdentity().getName().toString());
            System.out.println("if condition "+getOurIdentity().getName().getOrganisation().equals("PartyA"));
            CordaX500Name temp = CordaX500Name.parse("O=PartyA,L=London,C=GB") ;
            if (getOurIdentity().getName().getOrganisation().equals("PartyA")){
                counterParty = CordaX500Name.parse("O=PartyB,L=New York,C=US");
            }
            else {
                counterParty = CordaX500Name.parse("O=PartyA,L=London,C=GB");
            }
            Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
            attachmentState.setCounterParty(getServiceHub().getNetworkMapCache().getPeerByLegalName(counterParty));
            TransactionBuilder builder = new TransactionBuilder(notary)
                    .addOutputState(attachmentState)
                    .addCommand(new AttachmentContract.Commands.Create(),getOurIdentity().getOwningKey());

            System.out.println("counter party name : "+counterParty.getCommonName());
            FlowSession flowSession = initiateFlow(getServiceHub().getNetworkMapCache().getPeerByLegalName(counterParty));

            SignedTransaction signedTransaction = getServiceHub().signInitialTransaction(builder,getOurIdentity().getOwningKey());
            subFlow(new FinalityFlow(signedTransaction, Arrays.asList(flowSession)));

            System.out.println("the provided hash doesnt exist on vault");
            return signedTransaction.getId().toString();
        }


    }
}
