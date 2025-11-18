package edu.karthiknk81.azsamples.AzConnection;

import com.azure.core.credential.TokenCredential;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;

public class AzRMConnector {

    private String subs;
    private TokenCredential credential;
    private AzureProfile profile;
    private AzureResourceManager azManager;

    public AzRMConnector() {
        this.setCredential();
    }

    private void setSubs(String argSubs) { 
        this.subs = argSubs;
        this.setProfile();
    }

    private String getSubs() {
        return subs;
    }

    private AzureProfile getProfile() {
        return this.profile;
    }

    private void setProfile() {
        String subId = this.getSubs();
        if (subId == null) {
            System.out.println("ERROR: Subscription NOT set.");
            System.exit(-1);
        }
        this.profile = new AzureProfile(subId, subId, AzureEnvironment.AZURE);
    }

    private TokenCredential getCredential() {
        return this.credential;
    }

    private void setCredential() {
        this.credential = new DefaultAzureCredentialBuilder().build();
    }

    public void setAzManager(String subs) {
        this.setSubs(subs);
        this.azManager = AzureResourceManager
        .authenticate(this.getCredential(), this.getProfile())
        .withSubscription(this.getSubs());

    }

    public void printResourceGroups() {
        if(this.azManager == null) {
            System.out.println("ERROR: Azure Resource Mananger not initialize.");
            System.exit(-1);
        }
        this.azManager.resourceGroups().list().forEach(rg -> {
            System.out.printf("Name: %s, Location: %s, Provisioning State: %s\n",
                    rg.name(),
                    rg.regionName(),
                    rg.provisioningState());
        });
    }

}