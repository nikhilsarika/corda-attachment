package net.corda.examples.yo.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;

@InitiatedBy(StoreHash.class)
public class ValidateAttachmentHashResponder extends FlowLogic<SignedTransaction> {
    private final FlowSession counterpartySession;

    public ValidateAttachmentHashResponder(FlowSession counterpartySession) {
        this.counterpartySession = counterpartySession;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        return subFlow(new ReceiveFinalityFlow(counterpartySession));
    }
}
