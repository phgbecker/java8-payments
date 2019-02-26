package entity;

import java.math.BigDecimal;
import java.nio.file.Path;

public class Product {
    private String name;
    private Path file;
    private MediaFormat format;
    private BigDecimal price;

    public Product(String name, MediaFormat format, Path file, BigDecimal price) {
        this.name = name;
        this.format = format;
        this.file = file;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Path getFile() {
        return file;
    }

    public void setFile(Path file) {
        this.file = file;
    }

    public MediaFormat getFormat() {
        return format;
    }

    public void setFormat(MediaFormat format) {
        this.format = format;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return name;
    }
}
