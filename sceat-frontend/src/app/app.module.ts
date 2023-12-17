import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { KitchenComponent } from './kitchen/kitchen.component';
import { SchoolAdministrativeComponent } from './school-administrative/school-administrative.component';
import { StatisticsViewComponent } from './statistics-view/statistics-view.component';
import { PictureHolderComponent } from './kitchen/picture-holder/picture-holder.component';
import { DescriptionComponent } from './kitchen/description/description.component';
import { InfoHolderComponent } from './kitchen/info-holder/info-holder.component';
import { MatCardModule } from "@angular/material/card";
import { MatButtonModule} from "@angular/material/button";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgFor, NgOptimizedImage} from "@angular/common";
import {HTTP_INTERCEPTORS, HttpClientModule, HttpClientXsrfModule} from "@angular/common/http";
import {AuthInterceptorService} from "./auth-interceptor.service";
import { ItemManagerComponent } from './school-administrative/item-manager/item-manager.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatDatepickerModule} from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';



@NgModule({
  declarations: [
    AppComponent,
    KitchenComponent,
    SchoolAdministrativeComponent,
    StatisticsViewComponent,
    PictureHolderComponent,
    DescriptionComponent,
    InfoHolderComponent,
    ItemManagerComponent
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
      MatFormFieldModule, 
      MatSelectModule, 
      FormsModule, 
      ReactiveFormsModule, 
      NgFor,
      MatDatepickerModule,
      MatNativeDateModule
  ],
  providers: [{
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
