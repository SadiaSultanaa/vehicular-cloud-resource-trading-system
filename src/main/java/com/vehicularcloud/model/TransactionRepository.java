package com.vehicularcloud.model;

public interface TransactionRepository {
    void saveOwner(OwnerInput ownerInput);
    void saveClient(ClientInput clientInput);
}
