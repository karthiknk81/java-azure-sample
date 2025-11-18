package edu.karthiknk81.azsamples.AzConnection;

import com.azure.core.credential.TokenCredential;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AzRMConnector {
    
    private static final Logger logger = LoggerFactory.getLogger(AzRMConnector.class);
    
    private String subscriptionId;
    private TokenCredential credential;
    private AzureProfile profile;
    private AzureResourceManager azureResourceManager;
    
    // Public constructor for Spring instantiation
    public AzRMConnector() {
        initializeCredential();
    }
    
    // Constructor with subscription ID
    public AzRMConnector(String subscriptionId) {
        this.subscriptionId = subscriptionId;
        initializeCredential();
        initializeProfile();
        initializeAzureResourceManager();
    }
    
    // Spring setter for dependency injection
    public void setSubscriptionId(String subscriptionId) {
        if (subscriptionId == null || subscriptionId.trim().isEmpty()) {
            throw new IllegalArgumentException("Subscription ID cannot be null or empty");
        }
        this.subscriptionId = subscriptionId;
        initializeProfile();
        initializeAzureResourceManager();
    }
    
    public String getSubscriptionId() {
        return subscriptionId;
    }
    
    private void initializeCredential() {
        try {
            this.credential = new DefaultAzureCredentialBuilder().build();
            logger.info("Azure credentials initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize Azure credentials", e);
            throw new RuntimeException("Failed to initialize Azure credentials", e);
        }
    }
    
    private void initializeProfile() {
        if (subscriptionId == null || subscriptionId.trim().isEmpty()) {
            throw new IllegalStateException("Subscription ID must be set before initializing profile");
        }
        
        try {
            this.profile = new AzureProfile(subscriptionId, AzureEnvironment.AZURE);
            logger.info("Azure profile initialized for subscription: {}", subscriptionId);
        } catch (Exception e) {
            logger.error("Failed to initialize Azure profile for subscription: {}", subscriptionId, e);
            throw new RuntimeException("Failed to initialize Azure profile", e);
        }
    }
    
    private void initializeAzureResourceManager() {
        if (credential == null || profile == null) {
            throw new IllegalStateException("Credential and profile must be initialized before Azure Resource Manager");
        }
        
        try {
            this.azureResourceManager = AzureResourceManager
                .authenticate(credential, profile)
                .withSubscription(subscriptionId);
            logger.info("Azure Resource Manager initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize Azure Resource Manager", e);
            throw new RuntimeException("Failed to initialize Azure Resource Manager", e);
        }
    }
    
    public void printResourceGroups() {
        if (azureResourceManager == null) {
            logger.error("Azure Resource Manager is not initialized");
            throw new IllegalStateException("Azure Resource Manager is not initialized. Call setSubscriptionId() first.");
        }
        
        try {
            logger.info("Fetching resource groups for subscription: {}", subscriptionId);
            azureResourceManager.resourceGroups().list().forEach(rg -> {
                System.out.printf("Name: %s, Location: %s, Provisioning State: %s%n",
                        rg.name(),
                        rg.regionName(),
                        rg.provisioningState());
            });
        } catch (Exception e) {
            logger.error("Failed to fetch resource groups", e);
            throw new RuntimeException("Failed to fetch resource groups", e);
        }
    }
    
    // Getter for testing purposes
    public AzureResourceManager getAzureResourceManager() {
        return azureResourceManager;
    }
    
    // Method to check if the connector is properly initialized
    public boolean isInitialized() {
        return azureResourceManager != null;
    }
}