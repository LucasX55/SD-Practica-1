import java.lang.Exception;
import java.net.Socket;
import java.io.*;

public class HiloServidor extends Thread {

	private Socket skCliente;
	
	public HiloServidor(Socket p_cliente)
	{
		this.skCliente = p_cliente;
	}
	
	/*
	* Lee datos del socket. Supone que se le pasa un buffer con hueco 
	*	suficiente para los datos. Devuelve el numero de bytes leidos o
	* 0 si se cierra fichero o -1 si hay error.
	*/
	public String leeSocket (Socket p_sk, String p_Datos)
	{
		try
		{
			InputStream aux = p_sk.getInputStream();
			DataInputStream flujo = new DataInputStream( aux );
			p_Datos = new String();
			p_Datos = flujo.readUTF();
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e.toString());
		}
      return p_Datos;
	}

	/*
	* Escribe dato en el socket cliente. Devuelve numero de bytes escritos,
	* o -1 si hay error.
	*/
	public void escribeSocket (Socket p_sk, String p_Datos)
	{
		try
		{
			OutputStream aux = p_sk.getOutputStream();
			DataOutputStream flujo= new DataOutputStream( aux );
			flujo.writeUTF(p_Datos);      
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e.toString());
		}
		return;
	}
	
	

	public String realizarOperacion(String p_Cadena)
	{
		String respuesta="";
		String[] operacion = p_Cadena.split("#");
		
		
		if(operacion.length != 3||!operacion[0].equals("REGISTRO"))
		{
			
			respuesta = "STATUS#ERROR#Error al registrar la estacion";
			
		}else
		{
			System.out.println("SRV: La operacion es: " + operacion[0] + operacion[1] + " " + operacion[2]);

			respuesta = "STATUS#OK#Estacion registrada correctamente";
		}	
		return (respuesta);
	}
	
	
	
    public void run() {
		String respuesta="";
		String Cadena="";
		
        try {
			
			
				Cadena = this.leeSocket (skCliente, Cadena);
				/*
				* Se escribe en pantalla la informacion que se ha recibido del
				* cliente
				*/
				respuesta = this.realizarOperacion(Cadena);
				this.escribeSocket (skCliente, respuesta);						
			
			skCliente.close();
			//System.exit(0); No se debe poner esta sentencia, porque en ese caso el primer cliente que cierra rompe el socket 
			//				  y desconecta a todos				
        }
        catch (Exception e) {
          System.out.println("Error: " + e.toString());
        }
      }
}
