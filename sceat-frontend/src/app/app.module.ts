import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { KitchenComponent } from './kitchen/kitchen.component';
import { SchoolAdministrativeComponent } from './school-administrative/school-administrative.component';
import { StatisticsViewComponent } from './statistics-view/statistics-view.component';
import { MatCardModule } from "@angular/material/card";
import { MatButtonModule} from "@angular/material/button";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonModule, NgFor, NgIf, NgOptimizedImage} from "@angular/common";
import {HTTP_INTERCEPTORS, HttpClientModule, HttpClientXsrfModule} from "@angular/common/http";
import {AuthInterceptorService} from "./auth-interceptor.service";
import { ItemManagerComponent } from './school-administrative/item-manager/item-manager.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatDatepickerModule} from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import {MatTableModule} from '@angular/material/table';
import {MatDialog, MAT_DIALOG_DATA, MatDialogRef, MatDialogModule} from '@angular/material/dialog';
import { StudentreaderComponent } from './kitchen/student-reader/studentreader/studentreader.component';
import { LoginFormComponent } from './login-form/login-form.component';


@NgModule({
  declarations: [
    AppComponent,
    KitchenComponent,
    SchoolAdministrativeComponent,
    StatisticsViewComponent,
    ItemManagerComponent,
    StudentreaderComponent,
    LoginFormComponent
  ],
  imports: [
      BrowserModule,
      AppRoutingModule,
      BrowserAnimationsModule,
      MatCardModule,
      MatButtonModule,
      NgOptimizedImage,
      HttpClientModule,
      BrowserModule,
      HttpClientXsrfModule.withOptions({
          cookieName: 'JSESSIONID', // Replace with the actual cookie name
          headerName: 'JSESSIONID', // Replace with the actual header name
      }),
      FormsModule,
      MatButtonModule,
      MatFormFieldModule,
      MatInputModule,
      MatSelectModule,
      MatSelectModule,
      ReactiveFormsModule,
      NgFor,
      MatDatepickerModule,
      MatNativeDateModule,
      CommonModule,
      MatTableModule,
      NgIf,
      MatDialogModule

  ],
  providers: [{
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
