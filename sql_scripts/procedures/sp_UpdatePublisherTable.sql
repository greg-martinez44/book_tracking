DELIMITER //
DROP PROCEDURE IF EXISTS sp_UpdatePublisherTable;
CREATE PROCEDURE sp_UpdatePublisherTable (IN this_imprint VARCHAR(500), IN this_publisher VARCHAR(500))
BEGIN
SELECT * FROM publishers where imprint = this_imprint;
SELECT imprint_id INTO @imp_id FROM publishers WHERE imprint = this_imprint;
SELECT * from publishers where imprint = this_imprint;
UPDATE publishers SET publishing_house = this_publisher WHERE imprint_id = @imp_id;
SELECT * from publishers where imprint = this_imprint;
END //