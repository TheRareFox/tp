package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.BTC_DAILY_ADDRESS;
import static seedu.address.testutil.TypicalPersons.BTC_SPECIAL_ADDRESS;
import static seedu.address.testutil.TypicalPersons.JOE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.addresses.Network;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class RetrievePublicAddressCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexValidNetwork_success() {
        model.addPerson(JOE);

        RetrievePublicAddressCommand retrieveCommand =
                new RetrievePublicAddressCommand(Index.fromOneBased(model.getFilteredPersonList().size()), Network.BTC);

        String expectedMessage = String.format(RetrievePublicAddressCommand.MESSAGE_RETRIEVE_PUBLIC_ADDRESS_SUCCESS,
                2, Network.BTC, JOE.getName(), BTC_DAILY_ADDRESS + "\n" + BTC_SPECIAL_ADDRESS);

        assertCommandSuccess(retrieveCommand, model, expectedMessage, model);
    }

    @Test
    public void execute_validIndexValidNetworkWithLabel_success() {
        model.addPerson(JOE);

        RetrievePublicAddressCommand retrieveCommand =
                new RetrievePublicAddressCommand(Index.fromOneBased(model.getFilteredPersonList().size()), Network.BTC,
                        BTC_DAILY_ADDRESS.label);

        String expectedMessage = String.format(RetrievePublicAddressCommand.MESSAGE_RETRIEVE_PUBLIC_ADDRESS_SUCCESS,
                1, Network.BTC, JOE.getName(), BTC_DAILY_ADDRESS);

        assertCommandSuccess(retrieveCommand, model, expectedMessage, model);
    }

    @Test
    public void execute_validIndexValidNetworkEmpty_success() {
        model.addPerson(JOE);

        RetrievePublicAddressCommand retrieveCommand =
                new RetrievePublicAddressCommand(Index.fromOneBased(model.getFilteredPersonList().size()), Network.BTC,
                        "Non-existent");

        String expectedMessage = String.format(RetrievePublicAddressCommand.MESSAGE_RETRIEVE_PUBLIC_ADDRESS_SUCCESS,
                0, Network.BTC, JOE.getName(), "-");

        assertCommandSuccess(retrieveCommand, model, expectedMessage, model);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        RetrievePublicAddressCommand retrieveCommand = new RetrievePublicAddressCommand(outOfBoundIndex, Network.BTC);

        assertCommandFailure(retrieveCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_personWithoutPublicAddresses_returnsEmptyList() {
        Person personWithoutPublicAddresses = new PersonBuilder().build();
        model.addPerson(personWithoutPublicAddresses);

        RetrievePublicAddressCommand retrieveCommand =
                new RetrievePublicAddressCommand(Index.fromOneBased(model.getFilteredPersonList().size()), Network.BTC);

        String expectedMessage = String.format(RetrievePublicAddressCommand.MESSAGE_RETRIEVE_PUBLIC_ADDRESS_SUCCESS,
                0, Network.BTC, personWithoutPublicAddresses.getName(), "-");

        assertCommandSuccess(retrieveCommand, model, expectedMessage, model);
    }

    @Test
    public void equals() {
        RetrievePublicAddressCommand retrieveFirstCommand =
                new RetrievePublicAddressCommand(INDEX_FIRST_PERSON, Network.BTC);
        RetrievePublicAddressCommand retrieveSecondCommand =
                new RetrievePublicAddressCommand(INDEX_SECOND_PERSON, Network.BTC);

        // same object -> returns true
        assertTrue(retrieveFirstCommand.equals(retrieveFirstCommand));

        // same values -> returns true
        RetrievePublicAddressCommand retrieveFirstCommandCopy =
                new RetrievePublicAddressCommand(INDEX_FIRST_PERSON, Network.BTC);
        assertTrue(retrieveFirstCommand.equals(retrieveFirstCommandCopy));

        // different types -> returns false
        assertFalse(retrieveFirstCommand.equals(1));

        // null -> returns false
        assertFalse(retrieveFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(retrieveFirstCommand.equals(retrieveSecondCommand));

        // TODO: Re-enable after more networks supported
        // different network -> returns false
        // RetrievePublicAddressCommand retrieveFirstCommandEth =
        //         new RetrievePublicAddressCommand(INDEX_FIRST_PERSON, Network.ETH);
        // assertFalse(retrieveFirstCommand.equals(retrieveFirstCommandEth));

        // different label -> returns false
        RetrievePublicAddressCommand retrieveFirstCommandLabelled =
                new RetrievePublicAddressCommand(INDEX_FIRST_PERSON, Network.BTC, "different");
        assertFalse(retrieveFirstCommand.equals(retrieveFirstCommandLabelled));
    }

    @Test
    public void toStringMethod() {
        RetrievePublicAddressCommand retrieveCommand =
                new RetrievePublicAddressCommand(INDEX_FIRST_PERSON, Network.BTC, "wallet1");
        String expected = new ToStringBuilder(retrieveCommand)
                .add("index", INDEX_FIRST_PERSON)
                .add("network", Network.BTC)
                .add("label", "wallet1")
                .toString();
        assertEquals(expected, retrieveCommand.toString());
    }

}
