FROM fluent/fluentd:v1.16-debian-2
USER root
RUN gem install fluent-plugin-kafka fluent-plugin-loki
USER fluent
#COPY fluentd.conf /fluentd/etc/fluent.conf