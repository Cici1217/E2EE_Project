package demo.pojo;

public class SignedPreKey {
    private int keyId;
    private String publicKey;
    private String signature;

    public SignedPreKey(int keyId, String publicKey, String signature) {
        this.keyId = keyId;
        this.publicKey = publicKey;
        this.signature = signature;
    }

    public int getKeyId() {
        return keyId;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "SignedPreKey{" +
                "keyId=" + keyId +
                ", publicKey='" + publicKey + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
