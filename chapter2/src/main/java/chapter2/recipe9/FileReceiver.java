package chapter2.recipe9;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import com.butor.netty.handler.codec.ftp.DataReceiver;

class FileReceiver implements DataReceiver {
    @Override
    public void receive(String name, InputStream data) throws IOException {
        System.out.println("got file: [" + name + "]");
        //copy to local folder
        Files.copy(data, new File("./data/"+name).toPath());        
    }
}