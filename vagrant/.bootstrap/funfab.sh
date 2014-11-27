#!/usr/bin/env bash

# constants
gradle_version=1.12

# Url
gradle_url="https://services.gradle.org/distributions/gradle-${gradle_version}-all.zip"

# Path
download_path="/vagrant/.download"

# Help functions

function file_download()
{
        if [ ! -f ${download_path}/${1##*/} ] ; then
                echo "Downloading $1"
                mkdir -vp "$download_path"
                wget "$1" --quiet -P "$download_path"
        fi

        if [ ! -d ${2} ] ; then
                echo "Unpacking $1 to $2"
                mkdir -vp "${2}"

                case "${1##*.}" in
                        gz)
                        if [ "${1##*.tar.}" == "gz" ] ; then
                                tar xzf "$download_path/${1##*/}" -C "$2"
                        else
                                gunzip -q -c "$download_path/${1##*/}" > "$2"
                        fi
                        ;;
                        zip)
                        unzip -qq "$download_path/${1##*/}" -d "$2"
                esac
        fi
}

# Main program

# Enable autologin

if [ `grep vagrant /etc/lightdm/lightdm.conf.d/20-lubuntu.conf | wc -l` -le 0 ]; then
   echo autologin-user=vagrant >> /etc/lightdm/lightdm.conf.d/20-lubuntu.conf
   echo autologin-user-timeout=0 >> /etc/lightdm/lightdm.conf.d/20-lubuntu.conf
   echo greeter-session=lightdm-gtk-greeter >> /etc/lightdm/lightdm.conf.d/20-lubuntu.conf
fi

# Install java8
add-apt-repository -y ppa:webupd8team/java 
apt-get -qqy update
echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections
echo debconf shared/accepted-oracle-license-v1-1 seen true | debconf-set-selections
apt-get -qqy install oracle-java8-installer oracle-java8-set-default 2> /dev/null
echo 'export JAVA_HOME=/usr/lib/jvm/java-7-oracle' >> ${HOME}/.bash_profile

#Install gradle
file_download "${gradle_url}" "/usr/local/lib/gradle"
ln -sfnv /usr/local/lib/gradle/gradle-${gradle_version} /usr/local/lib/gradle/latest
ln -sfnv /usr/local/lib/gradle/latest/bin/gradle /usr/local/bin/gradle


#Install eclipse
apt-get -qqy install eclipse

#Install eclipse gradle plugin
eclipse -nosplash -application org.eclipse.equinox.p2.director -repository http://dist.springsource.com/release/TOOLS/gradle -installIU org.springsource.ide.eclipse.gradle.feature.feature.group

#Install chrome
apt-get -qqy install chromium-browser

#Install git and vim
apt-get -qqy install git vim
git config --system core.editor vi
