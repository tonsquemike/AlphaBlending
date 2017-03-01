/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alphablending;

import Funciones.Funciones_Image;
import Funciones.Img;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Miguel
 */
public class Alpha {           
    private float alpha;
    float decremento = 0 ;
    int CONF;
    private boolean fast;
    
    public Alpha( String[] Args ){
        
        String        ConfigFile, Archivo1,Archivo2, DIR;
        MyListArgs    Param          ;
        float[]       pixeles = null ;
        BufferedImage image   = null ;
        BufferedImage image2  = null ;
        BufferedImage salida  = null ;
        boolean       formato = false;
        String        mosaico = ""   ;
        
        Param      = new MyListArgs(Args);
        ConfigFile = Param.ValueArgsAsString("-CONFIG", "");
        
        if (!ConfigFile.equals("")) {
            Param.AddArgsFromFile(ConfigFile);
        }//fin if
        
        String Sintaxis = "  -BACK:str -FORE:str -DIR:str [-ALPHA:float] [-CONF:int] [-MOSAICO:str] [-FAST:bool]";
        MySintaxis Review = new MySintaxis(Sintaxis, Param);
        //PARAMETROS FORZOSOS
        DIR  = Param.ValueArgsAsString("-DIR", "");
        CONF = Param.ValueArgsAsInteger("-CONF", 0);  
        if(CONF>4|CONF<0){
            CONF = 0;
                System.out.println("El valor de" +CONF+ "en alpha no es valido, y se igualo a cero");
        }
        
        mosaico = Param.ValueArgsAsString("-MOSAICO", "");
        Archivo1 = Param.ValueArgsAsString("-BACK", "");
        //LECTURA DE UN ARCHIVO .IMG
        if(Archivo1.endsWith(".img")|Archivo1.endsWith(".IMG")){
            image = Img.abreIMG(Archivo1);
        }
        else{
            try {
                image = ImageIO.read(new File(Archivo1));
            } catch (IOException ex) {}
        }
        
        Archivo2 = Param.ValueArgsAsString("-FORE", "");
        if(Archivo2.endsWith(".img")|Archivo2.endsWith(".IMG")){
            image2 = Img.abreIMG(Archivo2);
        }
        else{
            try {
                image2 = ImageIO.read(new File(Archivo2));
            } catch (IOException ex) {}
        }
        
        alpha = Param.ValueArgsAsFloat("-ALPHA", (float)0.5);
        //asigna valor a las variables deacuerdo al valor de CONF        
        decremento = alphaReduction(image);
        fast       = Param.ValueArgsAsBoolean("-FAST", false);                
        
        salida = insertMosaic( image, image2, mosaico );
        
        if (DIR.endsWith(".img") || DIR.endsWith(".IMG")) {
            formato = true;
        } else if (DIR.endsWith(".jpg") || DIR.endsWith(".JPG") || DIR.endsWith(".TIFF")
                || DIR.endsWith(".tiff") || DIR.endsWith(".gif") || DIR.endsWith(".GIF")
                || DIR.endsWith(".BMP") || DIR.endsWith(".bmp") || DIR.endsWith(".png")
                || DIR.endsWith(".PNG") || DIR.endsWith(".WBMP") || DIR.endsWith(".wbmp")) {
            formato = false;
        }
        if (salida!=null ){
            if (formato == true) 
            {
                pixeles = Funciones_Image.BufferAVector(salida);
                IOBinFile.WriteBinFloatFileIEEE754(DIR, pixeles);
            } //segundo if
            else if (formato == false) 
            {           
                Funciones_Image.saveBufferedImage(salida, DIR);
            }//del else
        }//primer if
    }
    
    public Alpha()
    {
        
    }
    
    /**
     * 
     * @param bI
     * @param bi 
     */
    public BufferedImage insertMosaic( BufferedImage b, BufferedImage f, String tipo ){
        BufferedImage primerPlano = null;
        BufferedImage salida      = null;
        int primerOperando        = 1   ;
        int segundoOperando       = 1   ;
        String[] numeros = new String[3];
                
        //try{
            if(!"".equals(tipo)&!isFast()){
                numeros = tipo.split("\\D");//poner Expresion regular para obtener solo los numeros
                primerOperando  = Integer.parseInt( numeros[0] );
                segundoOperando = Integer.parseInt( numeros[1] );
                primerPlano = correctSize( f, b.getWidth()/primerOperando, b.getHeight()/segundoOperando); 
                primerPlano = createMosaic( primerPlano, b.getWidth(), b.getHeight() );
            }
            
            if(isFast()){
                //primerPlano = f;
                salida = simpleAlphaBlend(f, b);
                System.out.println("fast");
            }
            else
                salida = writeAlpha(b,primerPlano);
            
        //}catch( Exception e)
        //{
            //if(salida == null)
            //    salida = f;
        //}
         
        return salida;    
    }
    
    
    /**
     * Crea un mosaico con una imagen
     * @param img imagen que se repetira
     * @param tipo
     * @return Mosaico de 2X2 de una imagen
     */
    private BufferedImage createMosaic( BufferedImage img, int Width, int Heigth ){
        BufferedImage resultado = new BufferedImage( Width, Heigth, BufferedImage.TYPE_INT_RGB );
        
        for( int x = 0, i = 0; x<resultado.getWidth(); x++, i++ )
        {       
             if( i==img.getWidth() )
                 i = 0;
             for( int y = 0, j = 0; y<resultado.getHeight(); y++, j++)
             {       
                  if( j==img.getHeight() )
                      j = 0;
                  resultado.setRGB(x, y, img.getRGB(i, j));                 
             }
        }
        
        return resultado;
    }
    /**
     * Cambia el tamaño de una imagen de acuerdo a un porcentaje de entrada
     * @param bi Bufferer que se modificará
     * @param porcentaje porcentaje del nuevo tamaño
     * @return Imagen con el nuevo tamaño deacuerdo al porcentaje recibido
     */
    private BufferedImage correctSize( BufferedImage bi, double porcentaje ){
        double aux;
        double width            = 0;
        double heigth           = 0;
        int w = 0;
        int h = 0;
        BufferedImage tnsImg = null;
        System.out.println(porcentaje/50);
        aux = porcentaje/100;
        
        width  = bi.getWidth() *aux;
        heigth = bi.getHeight()*aux;
        
        w = (int)width;
        h = (int)heigth;
        
        tnsImg = new BufferedImage(w,h, BufferedImage.TYPE_INT_RGB); 

        Graphics2D graphics2D = tnsImg.createGraphics(); 
        //graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        //dibuja en tnsImg la imagen el BufferedImage bi con un tamaño de widthxheigth
        graphics2D.drawImage(bi, 0, 0, w, h, null); 
        
        return tnsImg;
    }
    /**
     * 
     * @param bi Imagen a la que se le cambiará el tamaño
     * @param width Altura de la nueva imagen
     * @param heigthAnchura de la nueva imagen
     * @return Imagen con el nuevo tamaño deacuerdo a los parametros recibidos
     */
    private BufferedImage correctSize( BufferedImage bi, int width, int heigth ){
        BufferedImage tnsImg = null;
        
        tnsImg = new BufferedImage(width,heigth, BufferedImage.TYPE_INT_RGB); 

        Graphics2D graphics2D = tnsImg.createGraphics(); 
        //graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        //dibuja en tnsImg la imagen el BufferedImage bi con un tamaño de widthxheigth
        graphics2D.drawImage(bi, 0, 0, width, heigth, null); 
        
        return tnsImg;
    }
    /**
     * Calcula el valor que tendrá que decrementarse la variable alpha y corrige
     * el valor de la variable alpha en caso de que se encuentre fuera de rango
     * @param BufferedImage bI Buffer de una imagen donde se encontraran el numero de pixeles
     */   
    private float alphaReduction(BufferedImage bI){
        
        if(alpha>1){//corrección de variable alpha en caso de que este fuera de rango
            setAlpha(1);
            System.out.println("ALPHA ES MAYOR A 1 Y SE IGUALO A 1");
        }
        else if(alpha<0){
            setAlpha(0);
            System.out.println("ALPHA ES MENOR A 0 Y SE IGUALO A 0");
        }
        
        //Se inicializa la variable alpha para que el efecto se aplique correctamente
        if(CONF==1|CONF==3)
            setAlpha(1);//se establece a 1 porque se decrementará
        if( CONF==2|CONF==4)
            setAlpha(0);//el valor alpha se incrementrá 
        
        //en esta parte se calcula la reducción que se aplicará a la variable alpha en cada ciclo FOR
        if(CONF==1|CONF==2){//efecto horizontal
            return (float) 1/bI.getWidth();
        }
        else if(CONF==3|CONF==4)//efecto vertical
            return (float) 1/bI.getHeight();
        else
            return 0;        
    }
    /**
     * Aplica el efecto alpha blending
     * combina dos imagenes
     * @param BufferedImage backGround Buffer de la imagen que estará en el fondo
     * @param BufferedImage foreGround Buffer de la imagen que se ubicará en primer plano
     */    
    private BufferedImage writeAlpha(BufferedImage backGround,BufferedImage foreGround) {
        //Crea un buffer de imagen donde se guardará el resultado
        BufferedImage biDestino = new BufferedImage(backGround.getWidth(), backGround.getHeight(), BufferedImage.TYPE_INT_RGB);        
        int r = 0, g = 0, b =0;
        
        if(CONF==3|CONF==4)//EFECTO HORIZONTAL
        {
            if(CONF==3)//estaba dentro del for
                    setAlpha(alpha - decremento);
            else
                setAlpha(alpha + decremento);
            
            for( int y = 0; y < backGround.getHeight(); y++ ){
                
                for( int x = 0; x < backGround.getWidth(); x++ ) {
                    //Obtiene el color de la imagen original
                    Color c1 = new Color(backGround.getRGB(x, y));
                    Color c2 = new Color(foreGround.getRGB(x, y));

                    //almacena cada uno de los valores aplicando la formula
                    r = (int) ((alpha*c1.getRed())+(1-alpha)*c2.getRed());
                    g = (int) ((alpha*c1.getGreen())+(1-alpha)*c2.getGreen());
                    b = (int) ((alpha*c1.getBlue())+(1-alpha)*c2.getBlue());
                    biDestino.setRGB(x, y, new Color(r, g, b).getRGB());
                }
            }
        }
        else   //EFECTO VERTICAL
        {
            if(CONF==1)//estaba dentro del for
                    setAlpha(alpha - decremento);
            else//para CONF==2
                setAlpha(alpha + decremento);
            
            for( int x = 0; x < backGround.getWidth(); x++ ){               
                for( int y = 0; y < backGround.getHeight(); y++ ) {
                    //Obtiene el color de la imagen original
                    Color c1 = new Color(backGround.getRGB(x, y));
                    Color c2 = new Color(foreGround.getRGB(x, y));

                    //almacena cada uno de los valores aplicando la formula
                    r = (int) ((alpha*c1.getRed())+(1-alpha)*c2.getRed());
                    g = (int) ((alpha*c1.getGreen())+(1-alpha)*c2.getGreen());
                    b = (int) ((alpha*c1.getBlue())+(1-alpha)*c2.getBlue());
                    
                    biDestino.setRGB(x, y, new Color(r, g, b).getRGB());
                }
            }
        }
        
        return biDestino;
    }
    
    public BufferedImage simpleAlphaBlend (BufferedImage bi1, BufferedImage bi2)
    {

      BufferedImage bi3 = new BufferedImage (bi1.getWidth(), bi1.getHeight(),
                         BufferedImage.TYPE_INT_RGB);
      Graphics2D g2d = bi3.createGraphics ();
      g2d.drawImage (bi1, null, 0, 0);
      g2d.setComposite (AlphaComposite.getInstance (AlphaComposite.SRC_OVER, (1-alpha)));
      g2d.drawImage (bi2, null, 0, 0);
      g2d.dispose ();

      return bi3;
    }
    public int formula( int background, int foreground ){   
        return (int) ((alpha*background)+(1-alpha)*foreground);
    }

    /**
     * @param alpha the alpha to set
     */
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    /**
     * @return the fast
     */
    public boolean isFast() {
        return fast;
    }

    /**
     * @param fast the fast to set
     */
    public void setFast(boolean fast) {
        this.fast = fast;
    }
    
}
