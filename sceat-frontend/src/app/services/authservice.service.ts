import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {Observable, tap} from 'rxjs';
import axios from "axios";

@Injectable({
  providedIn: 'root'
})
export class AuthserviceService {

    private apiUrl = 'http://localhost:8080/api';
    private headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    //private axiosInstance = axios.create(this.axiosConfig);

    constructor(private http: HttpClient) { }

    login(email: string, password: string): Observable<any> {
        const body = { email, password };
        return this.http.post(`${this.apiUrl}/auth/login?email=${body.email}&password=${body.password}`, body,
            {
                headers: this.headers,
                withCredentials: true,
                observe: 'response'
            });
    }

    getData(): Observable<any> {

        return this.http.get(`${this.apiUrl}/user/self`);
    }
}

/**/
