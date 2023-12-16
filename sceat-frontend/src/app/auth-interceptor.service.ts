import { Injectable } from '@angular/core';
import {
    HttpInterceptor,
    HttpRequest,
    HttpHandler,
    HttpEvent,
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptorService implements HttpInterceptor {
    intercept(
        request: HttpRequest<any>,
        next: HttpHandler
    ): Observable<HttpEvent<any>> {
        // Add your authentication logic here
        const modifiedRequest = request.clone({
            // Set headers, add tokens, or perform other authentication-related tasks
            withCredentials: true, // Example: Include credentials in the request
        });

        return next.handle(modifiedRequest);
    }
}
