ALPHA BLENDING

#DESCRICI�N 
ALPHA BLENDING ES UN M�TODO SIMPLE QUE PERMITE MEZCLAR DOS IM�GENES CON UNA PEQUE�A TRANSPARENCIA, EN ESTE CASO SER�  BI Y FI. LA IMAGEN DE FONDO ES LA IMAGEN BI Y SER� CUBIERTA CON LA IMAGEN FI LA TRANSPARENCIA SER� CONTROLADO CON LA VARIABLE ALPHA () MEDIANTE LA SIGUIENTE FORMULA[1].

	R(I)=ALPHA*BI(I)+(1-ALPHA)*FI(I)

CON 0<=ALPHA<=1, EN CASO DE SER 0 LA IMAGEN FI NO SERA TRANSPARENTE, EN ESE CASO SOLO SE OBTENDR� COMO RESULTADO LA IMAGEN DE FONDO. EN CASO DE QUE ALPHA TENGA EL VALOR 1 SOLO LA IMAGEN FI SER� VISIBLE.


#SINTAXIS
-BACK:str -FORE:str -DIR:str [-ALPHA:float] [-CONF:int]

#PARAMETROS OBLIGATORIOS
-BACK
-FORE
-DIR

#PARAMETROS OPCIONALES
-APLHA
-CONF

#DESCRIPCI�N DE LA SINTAXIS
EIQUETA		DESCRIPCI�N

-BACK		IM�GEN QUE SE UBICAR� EN EL FONDO
-FORE 		IM�GEN QUE SE UBICAR� EN PRIMER PLANO
-DIR		DIRECTORIO DE SALIDA
-ALPHA		Intensidad de el efecto transparente
-CONF		CONFIGURACI�N DEL TIPO DE EFECTO QUE APLICAR� LA HERRAMIENTA DE ACUERDO A LOS SIGUIENTES CRITERIOS:
#CONF 1 = alpha de izquierda a derecha
#CONF 2 = alpha de derecha a izquierda
#CONF 3 = alpha de arriba hacia abajo
#CONF 4 = alpha de abajo hacia arriba

NOTAS:
#si CONF es mayor a 4 o menor a cero CONF se igualar� autom�ticamente a 0
#En caso de elegir una configuraci�n valida, el valor alpha de este archivo se ignora y se controla autom�ticamente desde el programa 
#El efecto alpha solo podr� estar dentro del rango 0<=alpha<=1
#EN CASO DE NO ESPECIFICAR LA CONSTANTE ALPHA EN EL ARCHIVO DE CONFIGURACI�N LA CONSTANTE ALPHA SE IGUALA A 0.5

EJEMPLOS DE ENTRADA VALIDOS
#WINDOWS

1.-
-BACK C:\temp\\r.jpg
-FORE C:\temp\\a.jpg
-DIR C:\temp\\alpha.img
-ALPHA 0.65
-CONF 4

2.-
#-BACK C:\temp\\IMG125.IMG
#-FORE C:\temp\\a.IMG
#-DIR C:\temp\\alpha.img

3.-
-BACK C:\temp\\r.jpg
-FORE C:\temp\\a.jpg
-DIR C:\temp\\alpha.img
-ALPHA 0.71

4.-
-BACK C:\temp\\r.jpg
-FORE C:\temp\\a.jpg
-DIR C:\temp\\alpha.img
-CONF 3

#REFERENCIAS
[1]Digital Image Processing (An algorithmic Introduction Using java) First edition, Wilhelm Burger and Mark J. Burge, Editorial Springer page 83

