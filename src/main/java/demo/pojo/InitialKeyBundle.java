package demo.pojo;

public class InitialKeyBundle {
    private String userId;
    private String registrationId;
    private String identityKey;
    private SignedPreKey signedPreKey;
    private PreKey preKey;

    public InitialKeyBundle(String userId,
                            String registrationId, String identityKey,
                            SignedPreKey signedPreKey, PreKey preKey){
        this.userId = userId;
        this.registrationId = registrationId;
        this.identityKey = identityKey;
        this.signedPreKey = signedPreKey;
        this.preKey = preKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getIdentityKey() {
        return identityKey;
    }

    public void setIdentityKey(String identityKey) {
        this.identityKey = identityKey;
    }

    public SignedPreKey getSignedPreKey() {
        return signedPreKey;
    }

    public void setSignedPreKey(SignedPreKey signedPreKey) {
        this.signedPreKey = signedPreKey;
    }

    public PreKey getPreKey() {
        return preKey;
    }

    public void setPreKey(PreKey preKey) {
        this.preKey = preKey;
    }

    @Override
    public String toString() {
        return "InitialKeyBundle{" +
                "userId='" + userId + '\'' +
                ", registrationId='" + registrationId + '\'' +
                ", identityKey='" + identityKey + '\'' +
                ", signedPreKey=" + signedPreKey +
                ", preKey=" + preKey +
                '}';
    }
}
