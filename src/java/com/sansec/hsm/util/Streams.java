package com.sansec.hsm.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class Streams
{
    private static int BUFFER_SIZE = 512;

    public static void drain(InputStream inStr)
        throws IOException
    {
        byte[] bs = new byte[BUFFER_SIZE];
        while (inStr.read(bs, 0, bs.length) >= 0)
        {
        }
    }

    public static byte[] readAll(InputStream inStr)
        throws IOException
    {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        pipeAll(inStr, buf);
        return buf.toByteArray();
    }

    public static byte[] readAllLimited(InputStream inStr, int limit)
        throws IOException
    {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        pipeAllLimited(inStr, limit, buf);
        return buf.toByteArray();
    }

    public static int readFully(InputStream inStr, byte[] buf)
        throws IOException
    {
        return readFully(inStr, buf, 0, buf.length);
    }

    public static int readFully(InputStream inStr, byte[] buf, int off, int len)
        throws IOException
    {
        int totalRead = 0;
        while (totalRead < len)
        {
            int numRead = inStr.read(buf, off + totalRead, len - totalRead);
            if (numRead < 0)
            {
                break;
            }
            totalRead += numRead;
        }
        return totalRead;
    }

    public static void pipeAll(InputStream inStr, OutputStream outStr)
        throws IOException
    {
        byte[] bs = new byte[BUFFER_SIZE];
        int numRead;
        while ((numRead = inStr.read(bs, 0, bs.length)) >= 0)
        {
            outStr.write(bs, 0, numRead);
        }
    }

    public static long pipeAllLimited(InputStream inStr, long limit, OutputStream outStr)
        throws IOException
    {
        long total = 0;
        byte[] bs = new byte[BUFFER_SIZE];
        int numRead;
        while ((numRead = inStr.read(bs, 0, bs.length)) >= 0)
        {
            total += numRead;
            if (total > limit)
            {
                throw new IOException("Data Overflow");
            }
            outStr.write(bs, 0, numRead);
        }
        return total;
    }
    
    public static byte[] file2bytes(File file) throws IOException {
    	InputStream is = new FileInputStream(file);
    	
    	long length = file.length();
    	if (length >Integer.MAX_VALUE) {
    		is.close();
    		throw new IOException("too long file:" + file.getName());
    	}
    	byte[] bytes  = new byte[(int)length];
    	
    	// Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
    
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
        	is.close();
            throw new IOException("Could not completely read file "+file.getName());
        }
    
        // Close the input stream and return bytes
        is.close();
    	
    	return bytes;
    }
}
