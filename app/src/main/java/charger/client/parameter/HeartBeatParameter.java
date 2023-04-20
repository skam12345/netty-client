package charger.client.parameter;

import charger.client.constant.Constant;

public class HeartBeatParameter {
    private int index = 0;
    private boolean flag = true;
    public byte [] heartbeat() {
        byte [] hexArray = new byte[21];
        String remain = "C10068A406005557080904238416";
        int io = 0;
        int remainIdx = 0;
        while(flag) {
            if(index == 0) {
                hexArray[index] = 0x68;
            }else if(index >= 1 && index < 7) {
                String hex = Constant.chargerId.substring(io, io + 2);
                hexArray[index] = (byte) Long.parseLong(hex, 16);
                io += 2;
            }else if(index >= 7 &&  index < 21) {
                String hex = remain.substring(remainIdx, remainIdx + 2);
                hexArray[index] = (byte) Long.parseLong(hex, 16);
                remainIdx += 2;
            }else if(index == 21) {
                flag = false;
            }
            index++;
        }
        return hexArray;     
    } 
}
