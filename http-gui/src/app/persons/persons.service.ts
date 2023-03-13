import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {Feedback, Person, PersonData} from '../types';
import {environment} from '../../environments/environment';
import {switchMap, take} from 'rxjs/operators';
import {BackendService} from '../backend.service';

@Injectable({
  providedIn: 'root'
})
export class PersonsService {

  constructor(
    private http: HttpClient,
    private backendService: BackendService
  ) { }

   list$(): Observable<Array<Person>> {
     return this.http.get<Array<Person>>(
       environment.personBaseUrl + '/persons'
     );
   }

   create(person: PersonData): Observable<Person|null> {
    return this.backendService.feedback(
      this.http.post<HttpResponse<Feedback>>(
        environment.personBaseUrl + '/persons',
        JSON.stringify(person),
        {
          headers: {
            'Content-Type': 'application/json'
          },
          observe: 'response'
        }
      )
    ).pipe(
      switchMap( url => {
        if (url === null) {
          return of(null);
        } else {
          return this.http.get<Person>(url).pipe(take(1));
        }
      }),
    );
   }

   update(oldData: Person, newData: PersonData): Observable<Person|null> {
     const data = {};
     if (oldData.username !== newData.username) {
       data['oldUsername'] = oldData.username;
       data['newUsername'] = newData.username;
     }
     if (oldData.fullname !== newData.fullname) {
       data['oldFullname'] = oldData.fullname;
       data['newFullame'] = newData.fullname;
     }
     return this.backendService.feedback(
      this.http.put<HttpResponse<Feedback>>(
        environment.personBaseUrl + '/persons/' + oldData.id,
        JSON.stringify(data),
        {
          headers: {
            'Content-Type': 'application/json'
          },
          observe: 'response'
        }
      )
    ).pipe(
      switchMap( url => {
        if (url === null) {
          return of(null);
        } else {
          return this.http.get<Person>(url).pipe(take(1));
        }
      }),
    );
   }

  sick(person: Person): Observable<Person|null> {
    return this.backendService.feedback(
      this.http.post<HttpResponse<Feedback>>(
        environment.personBaseUrl + '/persons/' + person.id + '/sick',
        '{}',
        {
          headers: {
            'Content-Type': 'application/json'
          },
          observe: 'response'
        }
      )
    ).pipe(
      switchMap( url => {
        if (url === null) {
          return of(null);
        } else {
          return this.http.get<Person>(url).pipe(take(1));
        }
      }),
    );
  }

  healthy(person: Person): Observable<Person|null> {
    return this.backendService.feedback(
      this.http.delete<HttpResponse<Feedback>>(
        environment.personBaseUrl + '/persons/' + person.id + '/sick',
        {
          observe: 'response'
        }
      )
    ).pipe(
      switchMap( url => {
        if (url === null) {
          return of(null);
        } else {
          return this.http.get<Person>(url).pipe(take(1));
        }
      }),
    );
  }

   delete(person: Person): Observable<Array<Person>> {
    return this.backendService.feedback(
      this.http.delete<HttpResponse<Feedback>>(
        environment.personBaseUrl + '/persons/' + person.id,
        {
          observe: 'response'
        }
      )
    ).pipe(
      switchMap( url => {
        return this.list$();
      }),
    );
   }
}
