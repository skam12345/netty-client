package charger.client.parameter;

import charger.client.constant.Constant;

public class StopParameter {
    private int index = 0;
    private boolean flag = true;
    public byte [] stop() {
        byte [] hexArray = new byte[27];
        String remain = "C100688A0C000300000000000000000100FF8E16";
        int io = 0;
        int remainIdx = 0;
        while(flag) {
            if(index == 0) {
                hexArray[index] = 0x68;
            }else if(index >= 1 && index < 7) {
                String hex = Constant.chargerId.substring(io, io + 2);
                hexArray[index] = (byte) Long.parseLong(hex, 16);
                io += 2;
            }else if(index >= 7 &&  index < 27) {
                String hex = remain.substring(remainIdx, remainIdx + 2);
                hexArray[index] = (byte) Long.parseLong(hex, 16);
                remainIdx += 2;
            }else if(index == 27) {
                flag = false;
            }
            index++;
        }
        return hexArray;     
    } 
}
