#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include  <unistd.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h>


#define PORT 8080
#define BUFFER_SIZE 1024
#define MAX 100

typedef struct {
    int id;
    char name[50];
    int age;
}Student;


Student students[MAX];
int countStudents = 0;

void addStruct(int id, char* name, int age, char* res){
    students[countStudents].id = id;
    strcpy(students[countStudents].name, name);
    students[countStudents].age = age;
    countStudents++;
    sprintf(res, "Student added: ID=%d, Name=%s, Age=%d", id, name, age);
}

void deleteStruct(int id, char* res){
    for (int i = 0; i < countStudents; i++) {
        if (students[i].id == id) {
            for (int j = i; j < countStudents - 1; j++) {
                students[j] = students[j + 1];
            }
            countStudents--;
            sprintf(res, "Student with ID=%d deleted", id);
            return;
        }
    }
    sprintf(res, "Student with ID=%d not found", id);
}

void modifyStruct(int id, char* name, int age, char* res){
    for (int i = 0; i < countStudents;i++){
        if (students[i].id == id){
            strcpy(students[i].name, name);
            students[i].age = age;
            sprintf(res, "Student modified: ID=%d, Name=%s, Age=%d", id, name, age);
            return; 
        }
    }
}


void searchStruct(int id, char* res){
    for (int i = 0; i < countStudents; i++) {
        if (students[i].id == id) {
            sprintf(res, "Student found: ID=%d, Name=%s, Age=%d", id, students[i].name, students[i].age);
            return;
        }
    }
    sprintf(res, "Student with ID=%d not found", id);
}

void countAge12Struct(char* res){
    int count = 0;
    for (int i = 0; i < countStudents; i++) {
        if (students[i].age == 12) {
            count++;
        }
    }
    sprintf(res, "Number of students aged 12: %d", count);
}

void sortStructByAge(char* res){
    for (int i = 0; i < countStudents-1;i++){
        for (int j = 0; j < countStudents-i-1;j++){
            if (students[j].age > students[j+1].age){
                Student temp = students[j];
                students[j] = students[j+1];
                students[j+1] = temp;
            }
        }
    }
    sprintf(res, "Students sorted by age");
}


void addFile(int id, char* name, int age, char* res){
    FILE* fp = fopen("students.txt", "a");
    if (fp == NULL) {
        sprintf(res, "Error opening file");
        return;
    }
    fprintf(fp, "%d %s %d\n", id, name, age);
    sprintf(res, "Student added to file: ID=%d, Name=%s, Age=%d", id, name, age);
    fclose(fp);
}

void deleteFile(int id, char* res){
    FILE* fp = fopen("students.txt", "r");
    if (fp == NULL) {
        sprintf(res, "Error opening file");
        return;
    }
    FILE* temp = fopen("temp.txt","w");
    int sid,age; char name[50], header[100];
    fgets(header, sizeof(header), fp); // Skip header
    fprintf(temp, "%s", header); // Write header to temp
    int found = 0;
    while (fscanf(fp, "%d %s %d", &sid, name, &age) == 3) {
        if (sid != id){
            fprintf(temp, "%d %s %d\n", sid, name, age);
        } else {
            found = 1;
        }
    }
    fclose(fp);
    fclose(temp);
    remove("students.txt");
    rename("temp.txt", "students.txt");
    if (found) {
        sprintf(res, "Student with ID=%d deleted from file", id);
    } else {
        sprintf(res, "Student with ID=%d not found in file", id);
    }
}

void modifyFile(int id, char* name, int age, char* res){
    FILE* fp = fopen("students.txt","r");
    if (fp == NULL) {
        sprintf(res, "Error opening file");
        return;
    }
    int sid, sage;
    char sname[50], header[100];
    FILE* tfp = fopen("temp.txt","w");
    fgets(header, sizeof(header), fp); // Skip header
    fprintf(tfp, "%s", header); // Write header to temp
    int found = 0;
    while (fscanf(fp, "%d %s %d", &sid, sname, &sage) == 3) {
        if (sid == id){
            fprintf(tfp, "%d %s %d\n", id, name, age);
            found = 1;
        }
        else{
            fprintf(tfp, "%d %s %d\n", sid, sname, sage);
        }
    }
    fclose(fp);
    fclose(tfp);
    remove("students.txt");
    rename("temp.txt", "students.txt");
    if (found) {
        sprintf(res, "Student with ID=%d modified in file", id);
    } else {
        sprintf(res, "Student with ID=%d not found in file", id);
    }
}

void searchFile(int id,char *res){
    FILE *fp=fopen("students.txt","r");
    if (fp == NULL) {
        sprintf(res, "Error opening file");
        return;
    }
    int sid,age; char name[50], header[100];
    fgets(header, sizeof(header), fp); // Skip header

    while(fscanf(fp,"%d %s %d",&sid,name,&age)==3){
        if(sid==id){
            sprintf(res,"Student found in file: ID=%d, Name=%s, Age=%d",sid,name,age);
            fclose(fp);
            return;
        }
    }
    fclose(fp);
    sprintf(res,"Student with ID=%d not found in file",id);
}

void countAge12File(char* res){
    FILE* fp = fopen("students.txt","r");
    if (fp == NULL) {
        sprintf(res, "Error opening file");
        return;
    }
    int sid,age; char name[50], header[100];
    fgets(header, sizeof(header), fp); // Skip header
    int count = 0;
    while (fscanf(fp, "%d %s %d", &sid, name, &age) == 3) {
        if (age == 12){
            count++;
        }
    }
    fclose(fp);
    sprintf(res, "Number of students aged 12 in file: %d", count);
}

void sortFileByAge(char *res){
    FILE *fp=fopen("students.txt","r");
    if (fp == NULL) {
        sprintf(res, "Error opening file");
        return;
    }
    Student arr[MAX]; int n=0;
    char header[100];
    fgets(header, sizeof(header), fp); // Skip header

    while(fscanf(fp,"%d %s %d",
          &arr[n].id,
          arr[n].name,
          &arr[n].age)==3) n++;

    fclose(fp);

    for(int i=0;i<n-1;i++)
        for(int j=i+1;j<n;j++)
            if(arr[i].age>arr[j].age){
                Student t=arr[i];
                arr[i]=arr[j];
                arr[j]=t;
            }

    FILE *temp=fopen("temp.txt","w");
    fprintf(temp, "%s", header); // Write header
    for(int i=0;i<n;i++)
        fprintf(temp,"%d %s %d\n",
                arr[i].id,
                arr[i].name,
                arr[i].age);

    fclose(temp);
    remove("students.txt");
    rename("temp.txt","students.txt");
    sprintf(res,"File sorted by age");
}


int main() {
    int sockserver, sockclient;
    struct sockaddr_in serverAddr, clientAddr;
    socklen_t clientAddrLen;
    char buf[BUFFER_SIZE];

    sockserver = socket(AF_INET, SOCK_STREAM, 0);
    if (sockserver < 0) {
        perror("Socket creation failed");
        exit(EXIT_FAILURE);
    }

    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(PORT);
    serverAddr.sin_addr.s_addr = htonl(INADDR_ANY);

    if (bind(sockserver, (struct sockaddr*)&serverAddr, sizeof(serverAddr)) < 0) {
        perror("Bind failed");
        close(sockserver);
        exit(EXIT_FAILURE);
    }

    if (listen(sockserver, 1) < 0) {
        perror("Listen failed");
        close(sockserver);
        exit(EXIT_FAILURE);
    }

    printf("Server listening on port %d\n", PORT);
    printf("Server PID: %d\n", getpid());

    clientAddrLen = sizeof(clientAddr);

    while(1){
        sockclient = accept(sockserver, (struct sockaddr*)&clientAddr, &clientAddrLen);
        if (fork()==0){
            close(sockserver);

            printf("Client connected: %s:%d\n", inet_ntoa(clientAddr.sin_addr), ntohs(clientAddr.sin_port));
            printf("Client PID: %d\n", getpid());
            printf("Client PPID: %d\n", getppid());
            printf("Client IP: %s\n", inet_ntoa(clientAddr.sin_addr));
        
            while(1){
                memset(buf, 0, BUFFER_SIZE);
                recv(sockclient, buf, BUFFER_SIZE, 0);
                int choice = atoi(buf);
                if (choice==0){
                    break;
                }
                recv(sockclient, buf, BUFFER_SIZE, 0);
                int id,age; char name[50];
                sscanf(buf,"%d %s %d",&id,name,&age);
                 switch(choice){
                    case 1: addStruct(id,name,age,buf); break;
                    case 2: deleteStruct(id,buf); break;
                    case 3: modifyStruct(id,name,age,buf); break;
                    case 4: searchStruct(id,buf); break;
                    case 5: countAge12Struct(buf); break;
                    case 6: sortStructByAge(buf); break;
                    case 7: addFile(id,name,age,buf); break;
                    case 8: deleteFile(id,buf); break;
                    case 9: modifyFile(id,name,age,buf); break;
                    case 10: searchFile(id,buf); break;
                    case 11: countAge12File(buf); break;
                    case 12: sortFileByAge(buf); break;
                    default: sprintf(buf,"Invalid\n");
                }

                send(sockclient, buf, strlen(buf) + 1, 0);

            }

            close(sockclient);
            exit(0);
        }
        close(sockserver);
    }
}