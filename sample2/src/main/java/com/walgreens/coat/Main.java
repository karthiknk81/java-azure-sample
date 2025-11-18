package com.walgreens.coat;

import com.azure.core.credential.TokenCredential;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;

public class Main {
    private static final String SUBSCRIPTION_ID = "96a4f83f-xxxx-4321-xxxxx-1e198xxxb31";
    public static void main(String[] args) {

        TokenCredential credential = new DefaultAzureCredentialBuilder().build();
        AzureProfile profile = new AzureProfile(SUBSCRIPTION_ID, SUBSCRIPTION_ID, AzureEnvironment.AZURE);
        AzureResourceManager azMgr = AzureResourceManager
                .authenticate(credential, profile)
                .withSubscription(SUBSCRIPTION_ID);

        azMgr.resourceGroups().list().forEach(rg -> {
            System.out.printf("Name: %s, Location: %s, Provisioning State: %s\n",
                    rg.name(),
                    rg.regionName(),
                    rg.provisioningState());
        });
    }

}