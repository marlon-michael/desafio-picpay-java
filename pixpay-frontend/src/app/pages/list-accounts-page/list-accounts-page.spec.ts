import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListAccountsPage } from './list-accounts-page';

describe('ListAccountsPage', () => {
  let component: ListAccountsPage;
  let fixture: ComponentFixture<ListAccountsPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListAccountsPage],
    }).compileComponents();

    fixture = TestBed.createComponent(ListAccountsPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
