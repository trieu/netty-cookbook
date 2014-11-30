package netty.cookbook.chapter2.recipe9;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.butor.netty.handler.codec.ftp.DataReceiver;

class ConsoleReceiver implements DataReceiver {
    @Override
    public void receive(String name, InputStream data) throws IOException {
        System.out.println("receiving file: [" + name + "]");
        System.out.println("receiving data:");
        IOUtils.copy(data, System.out);
        System.out.println("");
    }
}