package parallel;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;


import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

/** Вспомогательный класс для сериализации BufferedImage
 * @author mclaudt
 *
 */
@SuppressWarnings("serial")
class SerializableBufferedImage implements Serializable
{

    private BufferedImage image = null;

    public SerializableBufferedImage()
    {
         super();
    }
    
    public SerializableBufferedImage(BufferedImage im)
    {
         this();
         setImage(im);
    }

    public BufferedImage getImage()
    {
         return image;
    }

    public void setImage(BufferedImage img)
    {
         this.image = img;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
         ImageIO.write(getImage(), "jpg", new MemoryCacheImageOutputStream(out));
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
         setImage(ImageIO.read(new MemoryCacheImageInputStream(in)));
    } 
}

