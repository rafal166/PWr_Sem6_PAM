package com.example.project;

import java.util.Map;

public class Globals{
    private static Globals instance;
    private String user_id = "";
    private String user_firstname = "";
    private String user_surname = "";

    private Globals(){}

    /**
     * Metoda pozwala ustawić dane użytkownika
     * @param user - dane uzytkownika z firestore
     */
    public void setUserData(String id, Map<String, Object> user){
        this.user_id = id;
        this.user_firstname = (String) user.get("firstname");
        this.user_surname = (String) user.get("surname");
    }

    /**
     * Metoda zwraca Imię aktualnie zalogowanego użytkownika
     * @return String - imię
     */
    public String getUserName(){
        return this.user_firstname;
    }

    /**
     * Metoda zwraca Nazwisko aktualnie zalogowanego użytkownika
     * @return String - nazwisko
     */
    public String getUserSurname(){
        return this.user_surname;
    }

    /**
     * Metoda zwraca Id aktualnie zalogowanego użytkownika
     * @return String - nazwisko
     */
    public String getUserId(){
        return this.user_id;
    }

    /**
     * Metoda umożliwia usunięcie zapisanych danych użytkownika
     */
    public void wipeUserData(){
        this.user_id = "";
        this.user_firstname = "";
        this.user_surname = "";
    }

    /**
     * Metoda sprawdza czy użytkownik jest zalogowany w aplikacji
     * @return - status
     */
    public boolean isUserLoggedIn(){
        return this.user_id != "";
    }

    /**
     * Metoda zwraca instancję klasy
     * @return Globals - instancja
     */
    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }
}