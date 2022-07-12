FROM openjdk:17-slim

RUN apt-get update &&\
        apt-get install --no-install-recommends -y git sudo &&\
        apt-get clean &&\
        rm -rf /var/lib/apt/lists/*

ARG NAME=dev
ARG PASSWORD=password
ARG UID=1000
ARG GID=1000
RUN groupadd -g $GID $NAME && \
        useradd -m -s /bin/bash -u $UID -g $GID -G sudo $NAME && \
        echo "$NAME:$PASSWORD" | chpasswd && \
        echo "$NAME ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers
USER $NAME

RUN echo 'alias minigit="java -jar /workspaces/minigit/app/build/libs/app-all.jar"' >> ~/.bashrc

CMD ["/bin/bash"]
