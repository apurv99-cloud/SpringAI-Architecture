CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS hstore;

CREATE TABLE IF NOT EXISTS vector_store (
                                            id TEXT PRIMARY KEY, -- id should be TEXT (not UUID type)
                                            content TEXT,
                                            metadata JSONB,
                                            embedding VECTOR(3072)
    );

-- Create IVFFLAT index for fast search
CREATE INDEX IF NOT EXISTS vector_store_embedding_idx ON vector_store USING IVFFLAT (embedding vector_cosine_ops);