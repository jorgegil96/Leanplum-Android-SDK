FROM python:2.7.15

WORKDIR /tmp

COPY Tools/requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt
