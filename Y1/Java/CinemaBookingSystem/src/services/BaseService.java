package services;

public abstract class BaseService {
    protected String filePath;

    public BaseService(String filePath) {
        this.filePath = filePath;
    }

    // Abstract methods that all child services must implement (Polymorphism)
    public abstract void loadFromFile();
    public abstract void saveToFile();

    public String getFilePath() {
        return filePath;
    }
}