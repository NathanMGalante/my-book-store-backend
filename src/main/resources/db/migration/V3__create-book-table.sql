CREATE TABLE IF NOT EXISTS book (
    id BIGINT NOT NULL AUTO_INCREMENT,
    creation_date_time DATETIME NOT NULL,
    store_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    synopsis TEXT NOT NULL,
    author VARCHAR(255) NOT NULL,
    publication_date DATE NOT NULL,
    available BOOLEAN DEFAULT TRUE,
    rating INT NOT NULL CHECK (rating >= 0 AND rating <= 5),
    cover LONGTEXT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (store_id) REFERENCES store(id)
) ENGINE=InnoDB;