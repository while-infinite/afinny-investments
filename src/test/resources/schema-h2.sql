CREATE TABLE IF NOT EXISTS account_agree
(
    id         UUID PRIMARY KEY,
    agree_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    is_active  BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS brokerage_account
(
    id             UUID PRIMARY KEY REFERENCES account_agree (id),
    client_id      UUID           NOT NULL,
    name_account   VARCHAR        NOT NULL,
    quantity       INTEGER        NOT NULL,
    rubles         DECIMAL(13, 2) NOT NULL,
    invested_money DECIMAL(13, 2) NOT NULL
    );
CREATE TABLE IF NOT EXISTS deal
(
    id                   UUID PRIMARY KEY,
    brokerage_account_id UUID REFERENCES brokerage_account (id),
    deal_type            VARCHAR NOT NULL,
    asset_type           VARCHAR,
    amount               INTEGER,
    purchase_price       DECIMAL(13, 2),
    selling_price        DECIMAL(13, 2),
    sum                  DECIMAL(13, 2),
    date_deal            TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    commission           DECIMAL(13, 2)
    );
CREATE TABLE IF NOT EXISTS asset
(
    id          UUID PRIMARY KEY REFERENCES deal (id),
    sec_id      VARCHAR NOT NULL,
    name        VARCHAR NOT NULL,
    description TEXT    NOT NULL,
    asset_type  VARCHAR NOT NULL,
    board_id    VARCHAR NOT NULL
    );

