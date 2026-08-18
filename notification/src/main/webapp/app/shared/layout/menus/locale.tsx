import React from 'react';
import { DropdownItem } from 'react-bootstrap';
import { NavDropdown } from './menu-components';
import { locales, languages } from 'app/config/translation';

export const LocaleMenu = ({ currentLocale, onClick }: { currentLocale: string; onClick: (locale: string) => void }) =>
  Object.keys(languages).length > 1 && (
    <NavDropdown icon="flag" name={currentLocale ? languages[currentLocale].name : undefined}>
      {locales.map(locale => (
        <DropdownItem key={locale} eventKey={locale} onClick={() => onClick(locale)}>
          {languages[locale].name}
        </DropdownItem>
      ))}
    </NavDropdown>
  );
