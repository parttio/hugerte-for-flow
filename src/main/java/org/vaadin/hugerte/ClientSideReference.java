package org.vaadin.hugerte;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Interface, that marks a class to have a single string client side representation.
 */
@FunctionalInterface
public interface ClientSideReference {

    /**
     * Returns the client side representation of this instance.
     * @return client side representation
     */
    String getClientSideRepresentation();

    /**
     * Returns a joined string of the given client side references, separated by a single space.
     * @param clientSideReferences references to join
     * @return joined string
     */
    static String join(ClientSideReference[] clientSideReferences) {
        return Arrays.stream(clientSideReferences)
                .map(ClientSideReference::getClientSideRepresentation)
                .collect(Collectors.joining(" "));
    }
}
