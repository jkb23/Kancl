Zpracovani POST req: 
   Check username a heslo
    Pokud heslo je spatne zadane
        BadLogin counter ++
        Pokud BadLoginCounter => 5
            5 min lock (Timestamp v SQL)
    Pokud je spravne zadane
        BadLoginCounter = 0
        Vstup do systemu > redirect na hello world
        Pozdeji vytvoreni session