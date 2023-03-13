import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {Feedback, Room, RoomData} from '../types';
import {environment} from '../../environments/environment';
import {catchError, delay, map, retry, retryWhen, switchAll, switchMap, take} from 'rxjs/operators';
import {BackendService} from '../backend.service';

@Injectable({
  providedIn: 'root'
})
export class RoomsService {

  constructor(
    private http: HttpClient,
    private backendService: BackendService
  ) { }

   list$(): Observable<Array<Room>> {
     return this.http.get<Array<Room>>(
       environment.roomBaseUrl + '/rooms'
     );
   }

   create(room: RoomData): Observable<Room|null> {
    return this.backendService.feedback(
      this.http.post<HttpResponse<Feedback>>(
        environment.roomBaseUrl + '/rooms',
        JSON.stringify(room),
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
          return this.http.get<Room>(url).pipe(take(1));
        }
      }),
    );
   }

   update(oldData: Room, newData: RoomData): Observable<Room|null> {
    const data = {};
    if (oldData.name !== newData.name) {
      data['oldName'] = oldData.name;
      data['newName'] = newData.name;
    }
    return this.backendService.feedback(
      this.http.put<HttpResponse<Feedback>>(
        environment.roomBaseUrl + '/rooms/' + oldData.id,
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
          return this.http.get<Room>(url).pipe(take(1));
        }
      }),
    );
   }

   lock(room: Room): Observable<Room|null> {
    return this.backendService.feedback(
      this.http.post<HttpResponse<Feedback>>(
        environment.roomBaseUrl + '/rooms/' + room.id + '/lock',
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
          return this.http.get<Room>(url).pipe(take(1));
        }
      }),
    );
   }

   unlock(room: Room): Observable<Room|null> {
    return this.backendService.feedback(
      this.http.delete<HttpResponse<Feedback>>(
        environment.roomBaseUrl + '/rooms/' + room.id + '/lock',
        {
          observe: 'response'
        }
      )
    ).pipe(
      switchMap( url => {
        if (url === null) {
          return of(null);
        } else {
          return this.http.get<Room>(url).pipe(take(1));
        }
      }),
    );
   }

   delete(room: Room): Observable<Array<Room>> {
    return this.backendService.feedback(
      this.http.delete<HttpResponse<Feedback>>(
        environment.roomBaseUrl + '/rooms/' + room.id,
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
