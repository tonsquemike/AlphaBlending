package alphablending;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package readwritebinfiles;
import  java.io.*;

/**
 *
 * @author Renecito
 */
public class IOBinFile {
    
 public static float [] ReadBinFloatFileIEEE754(String FileName) {

        float [] arreglo = null;

          try {
            File archivo                = new File(FileName); //se crea la representacion abstracta del archivo a leer aunque la clase FileInputStream tiene un constructor que toma un String en lugar de un File para denotar el archivo a leer, es importante hacer un File, por razones que se explicaran a continuacion.
            long tamanyo                = archivo.length();   //obtener la longitud del archivo
            FileInputStream FIS         = new FileInputStream(archivo); //Especificar la fuente del flujo de entrada
            DataInputStream inputstream = new DataInputStream(FIS);     //se crea el stram de entrada. Puede ser un ObjectInputStream o un DataInputStream en este caso, se usara el DataInputStream, ya que ObjectInputStream espera una cabecera especifica en el archivo, cosa que corromperia los archivos hablando en un sentido de estricta portabilidad. Dado que el objetivo del proyecto es hacer los programas lo mas flexibles posible dicha perdida de portabilidad es inaceptable. Los metodos de ObjectInputStream y DataInputStream usados para leer tipos de datos primitivos son exactamente los mismos.    
            int tamanyoArreglo = (int) (tamanyo / 4);
            arreglo = new float [tamanyoArreglo];
            
            int bits,B1,B2,B3,B4;
            for(int ind = 0; ind < tamanyoArreglo; ind++){
                bits = inputstream.readInt();
                B1   = (( bits >> 24) & 0x000000ff);
                B2   = (( bits >> 16) & 0x000000ff);
                B3   = (( bits >> 8 ) & 0x000000ff);
                B4   = (( bits >> 0 ) & 0x000000ff);
                arreglo[ind] = Float.intBitsToFloat( (B4<<24)|(B3<<16)|(B2<<8)|(B1<<0) );
                }//del for
             
            FIS.close();
            inputstream.close();
             } catch (IOException e){//Catch exception if any
             System.err.println("Error: " + e.getMessage());
            }

            return arreglo;
 } //fin de la función
       
       
 public static void WriteBinFloatFileIEEE754(String FileName, float []Arreglo) {
       try {
            File salida          = new File(FileName);
            FileOutputStream FOS = new FileOutputStream(salida);
            DataOutputStream DOS = new DataOutputStream (FOS);
            int bits, B1,B2,B3,B4;    
            for(int ind = 0; ind < Arreglo.length; ind++){
                bits = Float.floatToIntBits(Arreglo[ind]);
                B1   = (( bits >> 24) & 0x000000ff);
                B2   = (( bits >> 16) & 0x000000ff);
                B3   = (( bits >> 8 ) & 0x000000ff);
                B4   = (( bits >> 0 ) & 0x000000ff);
                bits = ((B4<<24)|(B3<<16)|(B2<<8)|(B1<<0)) ;

                DOS.writeInt(bits);
                //DOS.writeFloat(Arreglo[ind]);
                }
            FOS.close();
            DOS.close();
            } catch (IOException e){//Catch exception if any
              System.err.println("Error: " + e.getMessage());
              }

 }//fin de la funcion
       
      
}
