/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MovieManagement;

import java.io.Serializable;

/**
 *
 * @author User
 */
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String genre;
    private String runningTime;
    private String subtitles;
    private String director;
    private String casts;
    private String spokenLanguage;
    private String description;
    private String classifications;

    public Movie(String id, String title, String genre, String runningTime, String subtitles, String director, String casts, String spokenLanguage, String description, String classifications) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.runningTime = runningTime;
        this.subtitles = subtitles;
        this.director = director;
        this.casts = casts;
        this.spokenLanguage = spokenLanguage;
        this.description = description;
        this.classifications = classifications;
    }

    public Movie() {
        this.id = "";
        this.title = "";
        this.genre = "";
        this.runningTime = "";
        this.subtitles = "";
        this.director = "";
        this.casts = "";
        this.spokenLanguage = "";
        this.description = "";
        this.classifications = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(String runningTime) {
        this.runningTime = runningTime;
    }

    public String getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(String subtitles) {
        this.subtitles = subtitles;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCasts() {
        return casts;
    }

    public void setCasts(String casts) {
        this.casts = casts;
    }

    public String getSpokenLanguage() {
        return spokenLanguage;
    }

    public void setSpokenLanguage(String spokenLanguage) {
        this.spokenLanguage = spokenLanguage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClassifications() {
        return classifications;
    }

    public void setClassifications(String classifications) {
        this.classifications = classifications;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n------------------------------------------------------------------------\n");
        sb.append("|                          MOVIE INFORMATION                           |\n");
        sb.append("------------------------------------------------------------------------\n");
        sb.append(String.format("| ID              : %-50s |\n", id));
        sb.append(String.format("| Title           : %-50s |\n", title));
        sb.append(String.format("| Genre           : %-50s |\n", genre));
        sb.append(String.format("| Running Time    : %-50s |\n", runningTime));
        sb.append(String.format("| Subtitles       : %-50s |\n", subtitles));
        sb.append(String.format("| Director        : %-50s |\n", director));
        sb.append(String.format("| Casts           : %-50s |\n", casts));
        sb.append(String.format("| Spoken Language : %-50s |\n", spokenLanguage));
        sb.append(String.format("| Description     : %-50s |\n", description));
        sb.append(String.format("| Classification  : %-50s |\n", classifications));
        sb.append("------------------------------------------------------------------------\n");
        return sb.toString();
    }
}
