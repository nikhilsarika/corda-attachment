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
public class CheckHash extends FlowLogic<Boolean> {


    public String attachmentHash;

    public CheckHash(String attachmentHash) {
        this.attachmentHash = attachmentHash;
    }

    @Suspendable
    @Override
    public Boolean call() throws FlowException {


        Optional<StateAndRef<AttachmentState>> state = getServiceHub().getVaultService().queryBy(AttachmentState.class)
                .getStates().stream().filter(fil -> fil.getState().getData().getAttachmentHash().equals(attachmentHash)).findFirst();

        Map<String,Boolean> responseMap = new HashMap<>();
        if (state.isPresent()){
            return true;
        }
        else {
            return true;
        }


    }
}

