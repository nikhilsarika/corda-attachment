package net.corda.examples.yo.contracts;

import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

public class AttachmentContract implements Contract {

    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {
        if (tx.getCommand(0).getValue() instanceof Commands.Create){
            //do nothing
        }
    }

    public interface Commands extends CommandData{
        class Create implements Commands{}
    }
}
