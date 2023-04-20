package charger.client.parameter;

import charger.client.constant.Constant;

public class LoginParameter {
    private int index = 0;
    private boolean flag = true;
    public byte [] login() {
        byte [] hexArray = new byte[32];
        String remain = "C10068A1110011111117040908362100200000000000007E16";
        int io = 0;
        int remainIdx = 0;
        while(flag) {
            if(index == 0) {
                hexArray[index] = 0x68;
            }else if(index >= 1 && index < 7) {
                String hex = Constant.chargerId.substring(io, io + 2);
                hexArray[index] = (byte) Long.parseLong(hex, 16);
                io += 2;
            }else if(index >= 7 &&  index < 32) {
                String hex = remain.substring(remainIdx, remainIdx + 2);
                hexArray[index] = (byte) Long.parseLong(hex, 16);
                remainIdx += 2;
            }else if(index == 32) {
                flag = false;
            }
            index++;
        }
        return hexArray;     
    } 
}
