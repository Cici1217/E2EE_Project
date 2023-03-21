package demo.pojo;

import java.util.LinkedList;

public class KeyBundle {
    private String userId;
    private String registrationId;
    private String identityKey;
    private SignedPreKey signedPreKey;
    private LinkedList<PreKey> preKeys;


    public InitialKeyBundle initialKeyBundle(){
        return new InitialKeyBundle(userId, registrationId, identityKey, signedPreKey, preKeys.pop());
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

    public LinkedList<PreKey> getPreKeys() {
        return preKeys;
    }

    public void setPreKeys(LinkedList<PreKey> preKeys) {
        this.preKeys = preKeys;
    }

    @Override
    public String toString() {
        return "KeyBundle{" +
                "userId=" + userId +
                ", registrationId=" + registrationId +
                ", identityKey='" + identityKey + '\'' +
                ", signedPreKey=" + signedPreKey +
                ", preKeys=" + preKeys +
                '}';
    }
}
