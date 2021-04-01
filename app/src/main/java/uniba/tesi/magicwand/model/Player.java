package uniba.tesi.magicwand.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable {
    int id;
    int score;
    int wrong;

    public Player() {
    }

    public Player(int id, int score, int wrong) {
        this.id = id;
        this.score = score;
        this.wrong = wrong;
    }

    protected Player(Parcel in) {
        id = in.readInt();
        score = in.readInt();
        wrong = in.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getWrong() {
        return wrong;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }

   @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(score);
        dest.writeInt(wrong);
    }


}
