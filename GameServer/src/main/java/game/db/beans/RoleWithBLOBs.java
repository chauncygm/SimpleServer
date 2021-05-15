package game.db.beans;

public class RoleWithBLOBs extends Role {
    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}