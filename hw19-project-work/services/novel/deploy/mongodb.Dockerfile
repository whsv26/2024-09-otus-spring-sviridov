FROM mongo:7.0

RUN openssl rand -base64 756 > /data/keyfile && \
    chmod 400 /data/keyfile && \
    chown 999:999 /data/keyfile