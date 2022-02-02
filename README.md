## Notes Sharing Demo application

### A task
The app is an implementation of a coding task define below (citation)

```
Napisz aplikację do udostępniania notatek.
Interfejs aplikacji powinien pozwalać na:
● udostępnianie treści notatek,
● możliwość zdefiniowania czasu życia, po którym treść wygaśnie,
● zobaczenie treści po podaniu hasła.
Użyj języków podanych w opisie stanowiska oraz technologie i narzędzia według
własnego uznania.
Ocenie będą podlegać wszystkie elementy - od jakości dostarczonego kodu, poprzez
zastosowanie dobrych praktyk programowania po poprawność działania i interfejs
użytkownika.
Dodatkowe zadanie: wymyśl / opisz / zaprojektuj / zaimplementuj dodatkową
funkcjonalność, o którą warto byłoby wzbogacić aplikację.
W razie pytań skontaktuj się z nami.
```
Here are assumptions concluded from the task definition as well as from general logic:
1) The goal is a solution for note sharing
2) note should have a content and a title
3) note is a core domain thus `Note` to be a core entity.
4) thus `Note` has to have an id to be uniquely identifiable
5) note can be shared only by its owner
6) thus note should have a reference to its owner
7) note has a single owner
8) while sharing a note, its owner could specify its time-to-live
9) thus a shared note has different life cycle than a note in general
10) thus a shared note is essentially a copy of a note, a copy that expires
11) thus a `SharedNote` to be another entity with attributes decorated to `Note`
12) the user that shares a note has to be authenticated as an owner to be able to share a note
13) the service should allow the authenticated user to share a note she owns
14) the service should allow any other user in order to read the shared note to access it by a password
15) password is issued by the service during a note share and is associated with it
16) the service should allow also the authenticated user to read her own notes (and only owned notes)
17) user authentication is not a core of the service and has to be done by another service
18) for a sake of the current task authentication logic is not relevant thus the auth service will be stubbed to be able to authenticate two distinct users 
19) the service is headless, i.e. has a plain API without UI head (front end is ahead of competence and interest of an implementor)

### Implementation

GetNotesOwnedUseCase
ReadNoteOwnedUseCase
ShareNoteOwnedUseCase
ReadNoteReceivedUseCase

Services are runnable as a jar with embedded container,
or for more convenience as Dockerized images can be deployed to Kubernetes cluster/node.
Project contains a Redis and Fake SMTP deployables to Kubernetes.

### Runbook
As a prerequisite you will need:
1) JRE 17
2) Maven
3) Docker
4) Kubernetes set up in any form including `kubectl` executable.
   Minikube or one provided by Docker Desktop is sufficient.

#### Run Redis pod
1) `cd` to root project directory (Kubernetes resource declaration are there)
2) install redis server and CLI on K8s `kubectl apply -f k8s-redis-deployment.yaml`
3) check the port exposed: `kubectl get service redis-service -o jsonpath={.spec.ports[0].nodePort}`
4) check the kube DNS alias `kubectl cluster-info`.
   Output would be similar to this:
```
Kubernetes control plane is running at https://kubernetes.docker.internal:6443
CoreDNS is running at https://kubernetes.docker.internal:6443/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy
```
Where `kubernetes.docker.internal` is the host name in this case.

5) in `note-service/src/main/resources/application.yaml` do replace the hostname(`kube.dns-alias` property)
   and port number (`redis.server.port`) with respective values.

#### Run Fake SMTP pod
Similarly to redis server
1) `cd` to root project directory
2) run `kubectl apply -f k8s-fake-smtp-deployment.yaml`
3) run `kubectl get service fake-smtp-service -o jsonpath={.spec.ports[1].nodePort}`
   to find out the port for SMTP connection
4) replace `notification.email.port` property in the `application.yaml` with the actual value.
5) run `kubectl get service fake-smtp-service -o jsonpath={.spec.ports[0].nodePort}`
   to find out the port for HTTP communication to can query emails from the server.

#### Run Note service
1) `cd note-service` (Dockerfile is there)
2) Build an artifact `mvn clean install`
3) Build the docker image of the app `docker build -t note-service:0.1 .`
4) install the app on K8s `kubectl apply -f k8s-note-deployment.yaml`
5) check the `NodePort` exposed: `kubectl get service note-service -o jsonpath={.spec.ports[0].nodePort}`
   that is the port you can use for inbound HTTP communication.
6) check it out `kubectl get all`
   which will provide you a response similar to one below:
```
NAME                             READY   STATUS    RESTARTS   AGE
pod/fake-smtp-56c84dd6f7-8gb55   1/1     Running   0          2m36s
pod/note-8f4d686fb-g5d6x         1/1     Running   0          59s
pod/redis-756b4b8956-s7qzh       1/1     Running   0          2m55s

NAME                        TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)                         AGE
service/fake-smtp-service   NodePort    10.104.5.138    <none>        1080:31329/TCP,1025:30464/TCP   2m36s
service/kubernetes          ClusterIP   10.96.0.1       <none>        443/TCP                         36d
service/note-service        NodePort    10.100.211.62   <none>        8080:31476/TCP                  59s
service/redis-service       NodePort    10.106.148.38   <none>        6379:30431/TCP                  2m55s

NAME                        READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/fake-smtp   1/1     1            1           2m36s
deployment.apps/note        1/1     1            1           59s
deployment.apps/redis       1/1     1            1           2m55s

NAME                                   DESIRED   CURRENT   READY   AGE
replicaset.apps/fake-smtp-56c84dd6f7   1         1         1       2m36s
replicaset.apps/note-8f4d686fb         1         1         1       59s
replicaset.apps/redis-756b4b8956       1         1         1       2m55s
```

Feel free to grab application logs by running `kubectl logs note-8f4d686fb-g5d6x`
(replace with your pod name)

6) try it out
From Swagger UI:

http://<kube_DNS_alias>:<node_port>/swagger-ui/#/
Get all notes:
```
curl --location --request GET 'http://<kube_DNS_alias>:<node_port>/note/all' \
--header 'Authorization: 938fabe89a2cb'
```
Where DNS alias you gent with `kubectl cluster info` and node port
with `kubectl get service note-service -o jsonpath={.spec.ports[0].nodePort}` as described above.
`Authorization` header is used to authenticate a user of the app. The user can only see her own notes.

Read a note by id:
```
curl --location --request GET 'http://<kube_DNS_alias>:<node_port>/note/{id}' \
--header 'Authorization: 938fabe89a2cb'
```
Where `id` is one of the actual note ids retrieved from the request for all notes from `$.notes[x].metadata.id` path.

Share a note:

