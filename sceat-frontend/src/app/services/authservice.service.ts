import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {Observable, tap} from 'rxjs';
import axios, {AxiosRequestConfig} from "axios";

@Injectable({
  providedIn: 'root'
})
export class AuthserviceService {

    private apiUrl = 'http://trigary.duckdns.org:43210/api';
    private headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    private axiosConfig = {
        baseURL: "http://localhost:8080/api",
        timeout: 10000,
        withCredentials: true,
    };

    private axiosInstance = axios.create(this.axiosConfig);

    constructor(private http: HttpClient) { }


    private async post<T>(url: string, params: object, config: AxiosRequestConfig = {}): Promise<T | any> {
        try {
            config.method = "post";
            config.url = url;
            config.params = params;
            return (await this.axiosInstance.request(config)).data;
        } catch (error: any) {
            console.log(error);
        }
    }

    /*login(email: string, password: string): Observable<any> {
        const body = { email, password };
        return this.http.post(`${this.apiUrl}/auth/login?email=consumer-a@elte.hu&password=password`, body,
            {
                headers: this.headers,
                withCredentials: true,
                observe: 'response'
            });
    }*/

    login(name: string, password: string): Promise<void | any> {
        return this.post("/auth/login", { name: name, password: password });
    }
    getData(): Observable<any> {

        return this.http.get(`${this.apiUrl}/user/self`);
    }
}

/**/
