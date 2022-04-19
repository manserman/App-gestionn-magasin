package model;

public  class user {
    private String mnom;
    private String mmotdepasse;



    public user(String nom, String mdp)
    {this.mnom=nom;
    this.mmotdepasse=mdp;

    }

    public String getMnom() {
        return mnom;
    }

    public void setMnom(String mnom) {
        this.mnom = mnom;
    }
    public void setMmotdepasse(String mmotdepasse) {
        this.mmotdepasse = mmotdepasse;
    }

    public String getMmotdepasse() {
        return mmotdepasse;
    }
}
