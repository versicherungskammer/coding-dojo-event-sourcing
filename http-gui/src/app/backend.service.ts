import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {Feedback} from './types';
import {catchError, delay, map, retryWhen, switchMap, take} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class BackendService {

  constructor(
    private http: HttpClient
  ) { }

  feedback<T>(request: Observable<HttpResponse<T>>): Observable<string|null> {
    return request.pipe(
      take(1),
      switchMap( resp => {
        console.log('first resp ', resp);
        const feedbackUrl = resp.headers.get('Location');
        console.log('feedbackUrl ', feedbackUrl);
        return this.http.get<HttpResponse<Feedback>>(
          feedbackUrl,
          {
            observe: 'response'
          }
        ).pipe(
          retryWhen(errors => errors.pipe(delay(1000), take(10))),
          map( fbResp => {
            console.log('fbResp ', fbResp);
            if (fbResp.status === 425) {
              // not processed yet => retry after some short wait period
              throw new Error(fbResp.body['message']);
            } else if (fbResp.status === 303) {
              return resp.headers.get('Location');
            } else {
              return null;
            }
          }),
          catchError( _ => of(null))
        );
      })
    );
  }
}
