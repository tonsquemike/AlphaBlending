/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Funciones;

import alphablending.IOBinFile;
import java.awt.image.BufferedImage;

/**
 *
 * @author Miguel
 */
public class Img {
    /**
     * @since 20/06/2012
     * 
     */
    /**
     * metodo que permite leer una imagen en formato img para devolver un BufferedImage
     * @param archivo nNmbre del archivo que desea analizar
     * @return conversión de img a BufferedImage
     */
    public static BufferedImage abreIMG( String archivo ){
        float r[];
        r = IOBinFile.ReadBinFloatFileIEEE754( archivo );
        int ancho = (int) r[2];          
        int alto = (int) r[3];
        int vector[] = Funciones_Image.flotanteInt(r);
        BufferedImage image = Funciones_Image.VetorBuffer(alto, ancho, vector);
        return image;
    }
}
