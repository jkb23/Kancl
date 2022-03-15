package online.kancl;

import spark.Spark;

public class Main
{
	public static void main(String[] args)
	{
		Spark.port(8080);
		Spark.init();

		Spark.get("/", (req, res) -> "Hello World");

		System.out.println("Server running");
	}
}
