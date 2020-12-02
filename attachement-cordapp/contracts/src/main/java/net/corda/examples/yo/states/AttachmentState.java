package net.corda.examples.yo.states;

import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.examples.yo.contracts.AttachmentContract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@BelongsToContract(AttachmentContract.class)
public class AttachmentState implements LinearState {

    public String attachmentHash;
    public UniqueIdentifier uuid;
    public Party counterParty;
    public Party currentParty;

    public void setCounterParty(Party counterParty) {
        this.counterParty = counterParty;
    }

    public void setCurrentParty(Party currentParty) {
        this.currentParty = currentParty;
    }

    public String getAttachmentHash() {
        return attachmentHash;
    }

    public void setAttachmentHash(String attachmentHash) {
        this.attachmentHash = attachmentHash;
    }

    public UniqueIdentifier getUuid() {
        return uuid;
    }

    public void setUuid(UniqueIdentifier uuid) {
        this.uuid = uuid;
    }

    public Party getCounterParty() {
        return counterParty;
    }

    public Party getCurrentParty() {
        return currentParty;
    }

    public AttachmentState(String attachmentHash, UniqueIdentifier uuid) {
        this.attachmentHash = attachmentHash;
        this.uuid = uuid;
    }

    @NotNull
    @Override
    public UniqueIdentifier getLinearId() {
        return uuid;
    }

    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        List<AbstractParty> list = new ArrayList<>();
        list.add(currentParty);
        list.add(counterParty);
        return list;
    }
}
