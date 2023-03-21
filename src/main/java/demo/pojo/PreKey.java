package demo.pojo;

public class PreKey {
    private int keyId;
    private String publicKey;

    public PreKey(int keyId, String publicKey) {
        this.keyId = keyId;
        this.publicKey = publicKey;
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

    @Override
    public String toString() {
        return "PreKey{" +
                "keyId=" + keyId +
                ", publicKey='" + publicKey + '\'' +
                '}';
    }
}
